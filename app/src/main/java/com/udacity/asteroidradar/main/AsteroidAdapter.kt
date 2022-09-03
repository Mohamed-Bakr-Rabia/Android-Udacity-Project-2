package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.databinding.ItemListBinding

class AsteroidAdapter(private val clickListener:AsteroidListener) :
    ListAdapter<Asteroid,AsteroidAdapter.AsteroidViewHolder>(AsteroidDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {

        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)

    }

    class AsteroidViewHolder private constructor(private val binding: ItemListBinding) : ViewHolder(binding.root){

        fun bind(item: Asteroid, clickListener: AsteroidListener){
            binding.asteroid = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
             fun from(parent: ViewGroup): AsteroidViewHolder {
                val view = ItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return AsteroidViewHolder(view)
            }
        }
    }
}



class AsteroidDiffCallback:DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return newItem == oldItem
        }

}

class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit){
    fun onClick (asteroid: Asteroid) = clickListener(asteroid)
}